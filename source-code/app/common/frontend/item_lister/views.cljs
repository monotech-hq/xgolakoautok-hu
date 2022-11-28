
(ns app.common.frontend.item-lister.views
    (:require [app.common.frontend.item-lister.helpers    :as item-lister.helpers]
              [app.common.frontend.item-lister.prototypes :as item-lister.prototypes]
              [app.components.frontend.api                :as components]
              [elements.api                               :as elements]
              [engines.item-lister.api                    :as item-lister]
              [keyword.api                                :as keyword]
              [logical.api                                :refer [nor]]
              [random.api                                 :as random]
              [re-frame.api                               :as r]
              [x.components.api                           :as x.components]

              ; TEMP#0880
              ; A plugins.dnd-kit.api helyett az azt alkalmazó engines.item-sorter.api
              ; engine-t szükséges használni!
              [plugins.dnd-kit.api :as dnd-kit]))

;; -- Footer components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-lister-footer
  ; @param (keyword) lister-id
  ; @param (map) footer-props
  ;
  ; @usage
  ;  [item-lister-footer :my-lister {...}]
  [lister-id {}]
  (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? lister-id])]
          (let [all-item-count        @(r/subscribe [:item-lister/get-all-item-count        lister-id])
                downloaded-item-count @(r/subscribe [:item-lister/get-downloaded-item-count lister-id])
                download-info          {:content :npn-items-downloaded :replacements [downloaded-item-count all-item-count]}]
               [components/surface-description {:content download-info}])))

;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- create-item-button
  ; @param (keyword) lister-id
  ; @param (map) header-props
  ;  {:on-create (metamorphic-event)}
  [lister-id {:keys [on-create]}]
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? lister-id])]
       [components/surface-button ::create-item-button
                                  {:disabled? lister-disabled?
                                   :preset    :add
                                   :on-click  on-create}]))

(defn- search-field
  ; @param (keyword) lister-id
  ; @param (map) header-props
  ;  {:on-search (metamorphic-event)
  ;   :search-placeholder (metamorphic-content)}
  [lister-id {:keys [on-search search-placeholder]}]
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? lister-id])
        viewport-small?  @(r/subscribe [:x.environment/viewport-small?])]
       [elements/search-field ::search-field
                              {:autoclear?    true
                               :autofocus?    true
                               :disabled?     lister-disabled?
                               :border-radius (if viewport-small? :none :l)
                               :indent        {:top :xs}
                               :on-empty      on-search
                               :on-type-ended on-search
                               :placeholder   search-placeholder}]))

(defn- search-description
  ; @param (keyword) lister-id
  ; @param (map) header-props
  [lister-id _]
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled?   lister-id])
        search-term      @(r/subscribe [:item-lister/get-meta-item      lister-id :search-term])
        all-item-count   @(r/subscribe [:item-lister/get-all-item-count lister-id])
        description       (x.components/content {:content :search-results-n :replacements [all-item-count]})]
       [components/surface-description {:content (if (nor lister-disabled? (empty? search-term)) description)
                                        :disabled?        lister-disabled?
                                        :horizontal-align :left
                                        :indent           {:top :m :left :xs}}]))

(defn- breadcrumbs
  ; @param (keyword) lister-id
  ; @param (map) header-props
  ;  {:crumbs (maps in vector)}
  [lister-id {:keys [crumbs]}]
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? lister-id])]
       [components/surface-breadcrumbs ::breadcrumbs
                                       {:crumbs    crumbs
                                        :disabled? lister-disabled?}]))

(defn- label
  ; @param (keyword) lister-id
  ; @param (map) header-props
  ;  {:label (metamorphic-content)}
  [lister-id {:keys [label]}]
  (let [lister-disabled? @(r/subscribe [:item-lister/lister-disabled? lister-id])]
       [components/surface-label ::label
                                 {:disabled? lister-disabled?
                                  :label     label}]))

(defn item-lister-header
  ; @param (keyword) lister-id
  ; @param (map) header-props
  ;  {:crumbs (maps in vector)
  ;    [{:label (metamorphic-content)(opt)
  ;      :placeholder (metamorphic-content)
  ;      :route (string)(opt)}]
  ;   :on-create (metamorphic-event)(opt)
  ;   :on-search (metamorphic-event)
  ;   :label (metamorphic-content)
  ;   :search-placeholder (metamorphic-content)}
  ;
  ; @usage
  ;  [item-lister-header :my-lister {...}]
  [lister-id {:keys [crumbs on-create] :as header-props}]
  (if-let [first-data-received? @(r/subscribe [:item-lister/first-data-received? lister-id])]
          [:<> [:div {:style {:display "flex" :justify-content "space-between" :flex-wrap "wrap" :grid-row-gap "24px"}}
                     [:div [label       lister-id header-props]
                           [breadcrumbs lister-id header-props]]
                     (if on-create [:div [create-item-button lister-id header-props]])]
               [search-field       lister-id header-props]
               [search-description lister-id header-props]]
          [:<> [components/ghost-view {:layout :box-surface-header :breadcrumb-count (count crumbs)}]
               [components/ghost-view {:layout :box-surface-search-bar}]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- static-item-list
  ; @param (keyword) lister-id
  ; @param (map) body-props
  ;  {:list-item-element (component or symbol)}
  [lister-id {:keys [list-item-element] :as body-props}]
  (let [downloaded-items @(r/subscribe [:item-lister/get-downloaded-items lister-id])]
       (letfn [(f [item-list item-dex item]
                  (conj item-list [list-item-element lister-id body-props item-dex item]))]
              (reduce-kv f [:<>] downloaded-items))))

(defn- sortable-item-list
  ; @param (keyword) lister-id
  ; @param (map) body-props
  ;  {:list-item-element (component or symbol)
  ;   :items-path (vector)}
  [lister-id {:keys [items-path list-item-element] :as body-props}]
  (let [downloaded-items @(r/subscribe [:item-lister/get-downloaded-items lister-id])]
       [dnd-kit/body lister-id
                     {:items            downloaded-items
                      :item-id-f        :id
                      :item-element     [list-item-element lister-id body-props]
                      :on-order-changed (item-lister.helpers/on-order-changed-f lister-id body-props)}]))

(defn- item-list-body
  ; @param (keyword) lister-id
  ; @param (map) body-props
  ;  {:sortable? (boolean)(opt)}
  [lister-id {:keys [sortable?] :as body-props}]
  (if sortable? [sortable-item-list lister-id body-props]
                [static-item-list   lister-id body-props]))

(defn- item-list-header
  ; @param (keyword) lister-id
  ; @param (map) body-props
  ;  {:item-list-header (component or symbol)(opt)}
  [lister-id {:keys [item-list-header] :as body-props}]
  (if item-list-header [item-list-header lister-id body-props]))

(defn- item-list
  ; @param (keyword) lister-id
  ; @param (map) body-props
  [lister-id body-props]
  [components/item-list-table lister-id
                              {:body   [item-list-body   lister-id body-props]
                               :header [item-list-header lister-id body-props]}])

(defn- item-lister
  ; @param (keyword) lister-id
  ; @param (map) body-props
  [lister-id body-props]
  (let [body-props (assoc body-props :error-element [components/error-content {:error  :the-content-you-opened-may-be-broken}]
                                     :ghost-element [components/ghost-view    {:layout :item-list :item-count 3}]
                                     :list-element  [item-list lister-id body-props])]
       [item-lister/body lister-id body-props]))

(defn item-lister-body
  ; A komponens további paraméterezését az engines.item-lister/body komponens
  ; dokumentácójában találod!
  ;
  ; @param (keyword) lister-id
  ; @param (map) body-props
  ;  {:default-order-by (namespaced keyword)(opt)
  ;    Default: :modified-at/descending
  ;   :item-list-header (component or symbol)(opt)
  ;   :list-item-element (component or symbol)
  ;   :on-order-changed (metamorphic-event)(opt)
  ;    Az esemény utolsó paraméterként megkapja az újrarendezett elemeket.
  ;    W/ {:sortable? true}
  ;   :sortable? (boolean)(opt)
  ;    Default: false}
  ;
  ; @usage
  ;  [item-lister-body {...}]
  ;
  ; @usage
  ;  [item-lister-body :my-lister {...}]
  ;
  ; @usage
  ;  (defn my-item-element [lister-id body-props item-dex item] ...)
  ;  [item-lister-body :my-lister {:item-element  #'my-item-element}]
  ;
  ; @usage
  ;  (defn my-item-list-header  [lister-id body-props] ...)
  ;  (defn my-list-item-element [lister-id body-props item-dex item drag-props] ...)
  ;  [item-lister-body :my-lister {:item-list-header  #'my-item-list-header
  ;                                :list-item-element #'my-list-item-element
  ;                                :sortable?         true}]
  [lister-id body-props]
  (let [body-props (item-lister.prototypes/body-props-prototype lister-id body-props)]
       [item-lister lister-id body-props]))
