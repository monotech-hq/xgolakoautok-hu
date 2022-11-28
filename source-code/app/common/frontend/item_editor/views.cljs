
(ns app.common.frontend.item-editor.views
    (:require [app.common.frontend.item-editor.prototypes :as item-editor.prototypes]
              [app.components.frontend.api                :as components]
              [elements.api                               :as elements]
              [engines.item-editor.api                    :as item-editor]
              [re-frame.api                               :as r]
              [vector.api                                 :as vector]))

;; -- Footer components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-editor-footer
  ; @param (keyword) editor-id
  ; @param (map) footer-props
  ;
  ; @usage
  ;  [item-editor-footer :my-editor {...}]
  [editor-id _]
  (if-let [data-received? @(r/subscribe [:item-editor/data-received? editor-id])]
          (let [item-path @(r/subscribe [:item-editor/get-body-prop editor-id :item-path])]
               [components/item-info ::footer {:item-path item-path}])))

;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-item-props
  ; @param (keyword) editor-id
  ; @param (map) header-props
  ; @param (map) item-props
  ;  {:change-keys (keywords in vector)(opt)
  ;   :label (metamorphic-content)}
  ;
  ; @return (map)
  ;  {:active? (boolean)
  ;   :badge-color (keyword)
  ;   :disabled? (boolean)(opt)
  ;   :label (metamorphic-content)
  ;   :on-click (metamorphic-event)}
  [editor-id _ {:keys [change-keys disabled? label]}]
  (let [current-view @(r/subscribe [:x.gestures/get-current-view-id editor-id])
        form-changed? (if change-keys @(r/subscribe [:item-editor/form-changed? editor-id change-keys]))]
       {:active?     (= label current-view)
        :disabled?   disabled?
        :badge-color (if form-changed? :primary)
        :label       label
        :on-click    [:x.gestures/change-view! editor-id label]}))

(defn- menu-bar
  ; @param (keyword) editor-id
  ; @param (map) header-props
  ;  {:menu-items (maps in vector)
  ;    [{:change-keys (keywords in vector)(opt)
  ;      :disabled? (boolean)(opt)
  ;       Default: false
  ;      :label (metamorphic-content)}]}
  [editor-id {:keys [menu-items] :as header-props}]
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? editor-id])]
       (letfn [(f [menu-items menu-item] (conj menu-items (menu-item-props editor-id header-props menu-item)))]
              [elements/menu-bar ::item-editor-menu-bar
                                 {:disabled?  editor-disabled?
                                  :menu-items (reduce f [] menu-items)}])))

(defn- revert-item-button
  ; @param (keyword) editor-id
  ; @param (map) header-props
  [editor-id _]
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? editor-id])
        item-changed?    @(r/subscribe [:item-editor/item-changed?    editor-id])]
       [components/surface-button ::revert-item-button
                                  {:disabled? (or editor-disabled? (not item-changed?))
                                   :on-click  [:item-editor/revert-item! editor-id]
                                   :preset    :revert}]))

(defn- save-item-button
  ; @param (keyword) editor-id
  ; @param (map) header-props
  [editor-id _]
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? editor-id])]
       [components/surface-button ::save-item-button
                                  {:disabled? editor-disabled?
                                   :on-click  [:item-editor/save-item! editor-id]
                                   :preset    :save}]))

(defn- controls
  ; @param (keyword) editor-id
  ; @param (map) header-props
  [editor-id header-props]
  [:div {:style {:display "flex" :grid-column-gap "12px"}}
        [:<> [revert-item-button editor-id header-props]
             [save-item-button   editor-id header-props]]])

(defn- breadcrumbs
  ; @param (keyword) editor-id
  ; @param (map) header-props
  ;  {:crumbs (maps in vector)}
  [editor-id {:keys [crumbs]}]
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? editor-id])
        new-item?        @(r/subscribe [:item-editor/new-item?        editor-id])]
       [components/surface-breadcrumbs ::breadcrumbs
                                       {:crumbs (if new-item? (-> crumbs (vector/remove-last-item)
                                                                         (vector/conj-item {:label :add!}))
                                                              (-> crumbs (vector/conj-item {:label :edit!})))
                                        :disabled? editor-disabled?}]))

(defn- label
  ; @param (keyword) editor-id
  ; @param (map) header-props
  ;  {:label (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)}
  [editor-id {:keys [label placeholder]}]
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? editor-id])]
       [components/surface-label ::label
                                 {:disabled?   editor-disabled?
                                  :label       label
                                  :placeholder placeholder}]))

(defn item-editor-header
  ; @param (keyword) editor-id
  ; @param (map) header-props
  ;  {:crumbs (maps in vector)
  ;    [{:label (metamorphic-content)(opt)
  ;      :placeholder (metamorphic-content)(opt)
  ;      :route (string)(opt)}]
  ;   :label (metamorphic-content)(opt)
  ;   :menu-items (maps in vector)(opt)
  ;    [{:change-keys (keywords in vector)(opt)
  ;      :disabled? (boolean)(opt)
  ;      :label (metamorphic-content)}]
  ;   :placeholder (metamorphic-content)(opt)
  ;
  ; @usage
  ;  [item-editor-header :my-editor {...}]
  [editor-id {:keys [crumbs menu-items] :as header-props}]
  ; A menu-items használatához ne felejts el inicializálni egy gestures/view-handler
  ; kezelőt, az editor-id azonosítóval!
  (if-let [data-received? @(r/subscribe [:item-editor/data-received? editor-id])]
          [:<> [:div {:style {:display "flex" :justify-content "space-between" :flex-wrap "wrap" :grid-row-gap "48px"}}
                     [:div [label       editor-id header-props]
                           [breadcrumbs editor-id header-props]]
                     [:div [controls    editor-id header-props]]]
               [elements/horizontal-separator {:height :xxl}]
               (if menu-items [menu-bar editor-id header-props])]
          [:<> [components/ghost-view {:layout :box-surface-header :breadcrumb-count (count crumbs)}]
               [:div {:style {:width "100%" :height "96px"}}]]))

;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- item-editor
  ; @param (keyword) editor-id
  ; @param (map) body-props
  [editor-id body-props]
  (let [body-props (assoc body-props :error-element [components/error-content {:error  :the-item-you-opened-may-be-broken}]
                                     :ghost-element [components/ghost-view    {:layout :box-surface-body}])]
       [item-editor/body editor-id body-props]))

(defn item-editor-body
  ; A komponens további paraméterezését az engines.item-editor/body komponens
  ; dokumentácójában találod!
  ;
  ; @param (keyword) editor-id
  ; @param (map) body-props
  ;  {:auto-title? (boolean)(opt)
  ;    Default: true
  ;   :form-element (component or symbol)
  ;   :label-key (keyword)(opt)
  ;    Default: :name
  ;   :suggestion-keys (keywords in vector)(opt)
  ;    Default: [:name]}
  ;
  ; @usage
  ;  [item-editor-body :my-editor {...}]
  ;
  ; @usage
  ;  (defn my-form-element [] ...)
  ;  [item-editor-body :my-editor {:form-element #'my-form-element}]
  [editor-id body-props]
  (let [body-props (item-editor.prototypes/body-props-prototype editor-id body-props)]
       [item-editor editor-id body-props]))
