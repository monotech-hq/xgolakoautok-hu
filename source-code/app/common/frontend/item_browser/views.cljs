
(ns app.common.frontend.item-browser.views
    (:require [app.common.frontend.item-browser.prototypes :as item-browser.prototypes]
              [app.common.frontend.item-lister.views       :as item-lister.views]
              [app.components.frontend.api                 :as components]
              [elements.api                                :as elements]
              [engines.item-browser.api                    :as item-browser]
              [random.api                                  :as random]
              [re-frame.api                                :as r]))

;; -- Footer components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-browser-footer
  ; @param (keyword) lister-id
  ; @param (map) browser-props
  ;
  ; @usage
  ;  [item-browser-footer :my-browser {...}]
  [browser-id {}]
  (if-let [first-data-received? @(r/subscribe [:item-browser/first-data-received? browser-id])]
          (let [all-item-count        @(r/subscribe [:item-lister/get-all-item-count        browser-id])
                downloaded-item-count @(r/subscribe [:item-lister/get-downloaded-item-count browser-id])
                download-info          {:content :npn-items-downloaded :replacements [downloaded-item-count all-item-count]}]
               [components/surface-description {:content download-info}])))

;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- item-info
  ; @param (keyword) browser-id
  ; @param (map) header-props
  ;  {:item-info (metamorphic-content)(opt)}
  [browser-id {:keys [item-info]}]
  (if item-info [components/surface-description ::item-info
                                                {:content          item-info
                                                 :indent           {:top :m :right :xs}
                                                 :horizontal-align :right}]))

(defn- search-field
  ; @param (keyword) browser-id
  ; @param (map) header-props
  [browser-id header-props]
  [item-lister.views/search-field browser-id header-props])

(defn- search-description
  ; @param (keyword) browser-id
  ; @param (map) header-props
  [browser-id header-props]
  [item-lister.views/search-description browser-id header-props])

(defn- breadcrumbs
  ; @param (keyword) browser-id
  ; @param (map) header-props
  ;  {:crumbs (maps in vector)}
  [browser-id {:keys [crumbs]}]
  (let [browser-disabled? @(r/subscribe [:item-browser/browser-disabled? browser-id])]
       [components/surface-breadcrumbs ::breadcrumbs
                                       {:crumbs    crumbs
                                        :disabled? browser-disabled?}]))

(defn- label
  ; @param (keyword) browser-id
  ; @param (map) header-props
  [browser-id _]
  (let [browser-disabled? @(r/subscribe [:item-browser/browser-disabled?      browser-id])
        item-label        @(r/subscribe [:item-browser/get-current-item-label browser-id])]
       [components/surface-label ::label
                                 {:disabled? browser-disabled?
                                  :label     item-label}]))

(defn- item-browser-header
  ; @param (keyword) browser-id
  ; @param (map) header-props
  ;  {:crumbs (maps in vector)
  ;    [{:label (metamorphic-content)(opt)
  ;      :placeholder (metamorphic-content)
  ;      :route (string)(opt)}]
  ;   :item-info (metamorphic-content)(opt)
  ;   :on-search (metamorphic-event)
  ;   :search-placeholder (metamorphic-content)}
  ;
  ; @usage
  ;  [item-browser-header :my-browser {...}]
  [browser-id header-props]
  (if-let [first-data-received? @(r/subscribe [:item-browser/first-data-received? browser-id])]
          [:<> [label        browser-id header-props]
               [breadcrumbs  browser-id header-props]
               [search-field browser-id header-props]
               [:div {:style {:display "flex" :justify-content "space-between"}}
                     [search-description browser-id header-props]
                     [item-info          browser-id header-props]]]
          [:<> [components/ghost-view {:layout :box-surface-header :breadcrumb-count 2}]
               [components/ghost-view {:layout :box-surface-search-bar}]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- item-list-body
  ; @param (keyword) browser-id
  ; @param (map) body-props
  ;  {:list-item-element (component or symbol)}
  [browser-id {:keys [list-item-element] :as body-props}]
  (let [downloaded-items @(r/subscribe [:item-browser/get-downloaded-items browser-id])]
       (letfn [(f [item-list item-dex item]
                  (conj item-list [list-item-element browser-id body-props item-dex item]))]
              (reduce-kv f [:<>] downloaded-items))))

(defn- item-list-header
  ; @param (keyword) browser-id
  ; @param (map) body-props
  ;  {:item-list-header (component or symbol)(opt)}
  [browser-id {:keys [item-list-header] :as body-props}]
  (if item-list-header [item-list-header browser-id body-props]))

(defn- item-list
  ; @param (keyword) browser-id
  ; @param (map) body-props
  [browser-id body-props]
  [components/item-list-table browser-id
                              {:body   [item-list-body   browser-id body-props]
                               :header [item-list-header browser-id body-props]}])

(defn- item-browser
  ; @param (keyword) browser-id
  ; @param (map) body-props
  [browser-id body-props]
  (let [body-props (assoc body-props :error-element [components/error-content {:error :the-content-you-opened-may-be-broken}]
                                     :ghost-element [components/ghost-view    {:layout :item-list :item-count 3}]
                                     :list-element  [item-list browser-id body-props])]
       [item-browser/body browser-id body-props]))

(defn item-browser-body
  ; A komponens további paraméterezését az engines.item-browser/body komponens
  ; dokumentácójában találod!
  ;
  ; @param (keyword)(opt) browser-id
  ; @param (map) body-props
  ;  {:auto-title? (boolean)(opt)
  ;    Default: true
  ;   :default-order-by (namespaced keyword)(opt)
  ;    Default: :modified-at/descending
  ;   :item-list-header (component or symbol)(opt)
  ;   :label-key (keyword)(opt)
  ;    Default: :name
  ;   :list-item-element (component or symbol)}
  ;
  ; @usage
  ;  [item-browser-body {...}]
  ;
  ; @usage
  ;  [item-browser-body :my-browser {...}]
  ;
  ; @usage
  ;  (defn my-item-element [browser-id body-props item-dex item] ...)
  ;  [item-browser-body :my-browser {:item-element  #'my-item-element}]
  ;
  ; @usage
  ;  (defn my-item-list-header  [browser-id body-props] ...)
  ;  (defn my-list-item-element [browser-id body-props item-dex item] ...)
  ;  [item-browser-body :my-browser {:item-list-header  #'my-item-list-header
  ;                                  :list-item-element #'my-list-item-element}]
  ([body-props]
   [item-browser-body (random/generate-keyword) body-props])

  ([browser-id body-props]
   (let [body-props (item-browser.prototypes/body-props-prototype browser-id body-props)]
        [item-browser browser-id body-props])))
