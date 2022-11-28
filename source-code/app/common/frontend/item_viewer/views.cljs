
(ns app.common.frontend.item-viewer.views
    (:require [app.common.frontend.item-editor.views      :as item-editor.views]
              [app.common.frontend.item-viewer.prototypes :as item-viewer.prototypes]
              [app.components.frontend.api                :as components]
              [elements.api                               :as elements]
              [engines.item-viewer.api                    :as item-viewer]
              [re-frame.api                               :as r]
              [vector.api                                 :as vector]))

;; -- Footer components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-viewer-footer
  ; @param (keyword) viewer-id
  ; @param (map) footer-props
  ;
  ; @usage
  ;  [item-viewer-footer :my-viewer {...}]
  [viewer-id _]
  (if-let [data-received? @(r/subscribe [:item-viewer/data-received? viewer-id])]
          (let [item-path @(r/subscribe [:item-viewer/get-body-prop viewer-id :item-path])]
               [components/item-info ::footer {:item-path item-path}])))

;; -- Header components--------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  ; @param (keyword) viewer-id
  ; @param (map) header-props
  ;  {:menu-items (maps in vector)
  ;    [{:disabled? (boolean)(opt)
  ;       Default: false
  ;      :label (metamorphic-content)}]}
  [viewer-id {:keys [menu-items] :as header-props}]
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? viewer-id])]
       (letfn [(f [menu-items menu-item] (conj menu-items (item-editor.views/menu-item-props viewer-id header-props menu-item)))]
              [elements/menu-bar ::item-viewer-menu-bar
                                 {:disabled?  viewer-disabled?
                                  :menu-items (reduce f [] menu-items)}])))

(defn- delete-item-button
  ; @param (keyword) viewer-id
  ; @param (map) header-props
  [viewer-id _]
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? viewer-id])]
       [components/surface-button ::delete-item-button
                                  {:disabled? viewer-disabled?
                                   :on-click  [:item-viewer/delete-item! viewer-id]
                                   :preset    :delete}]))

(defn- duplicate-item-button
  ; @param (keyword) viewer-id
  ; @param (map) header-props
  [viewer-id _]
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? viewer-id])]
       [components/surface-button ::duplicate-item-button
                                  {:disabled? viewer-disabled?
                                   :on-click  [:item-viewer/duplicate-item! viewer-id]
                                   :preset    :duplicate}]))

(defn- edit-item-button
  ; @param (keyword) viewer-id
  ; @param (map) header-props
  ;  {:on-edit (metamorphic-event)}
  [viewer-id {:keys [on-edit]}]
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? viewer-id])]
       [components/surface-button ::edit-item-button
                                  {:disabled? viewer-disabled?
                                   :on-click  on-edit
                                   :preset    :edit}]))

(defn- controls
  ; @param (keyword) viewer-id
  ; @param (map) header-props
  [viewer-id header-props]
  [:div {:style {:display "flex" :grid-column-gap "12px"}}
        [:<> [delete-item-button    viewer-id header-props]
             [duplicate-item-button viewer-id header-props]
             [edit-item-button      viewer-id header-props]]])

(defn- breadcrumbs
  ; @param (keyword) viewer-id
  ; @param (map) header-props
  ;  {:crumbs (maps in vector)}
  [viewer-id {:keys [crumbs]}]
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? viewer-id])]
       [components/surface-breadcrumbs ::breadcrumbs
                                       {:crumbs    crumbs
                                        :disabled? viewer-disabled?}]))

(defn- label
  ; @param (keyword) viewer-id
  ; @param (map) header-props
  ;  {:label (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)}
  [viewer-id {:keys [label placeholder]}]
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? viewer-id])]
       [components/surface-label ::label
                                 {:disabled?   viewer-disabled?
                                  :label       label
                                  :placeholder placeholder}]))

(defn item-viewer-header
  ; @param (keyword) viewer-id
  ; @param (map) header-props
  ;  {:crumbs (maps in vector)
  ;    [{:label (metamorphic-content)(opt)
  ;      :placeholder (metamorphic-content)(opt)
  ;      :route (string)(opt)}]
  ;   :label (metamorphic-content)(opt)
  ;   :menu-items (maps in vector)(opt)
  ;    [{:disabled? (boolean)(opt)
  ;      :label (metamorphic-content)}]
  ;   :on-edit (metamorphic-event)(opt)
  ;   :placeholder (metamorphic-content)(opt)
  ;
  ; @usage
  ;  [item-viewer-header :my-viewer {...}]
  [viewer-id {:keys [crumbs menu-items] :as header-props}]
  ; A menu-items használatához ne felejts el inicializálni egy gestures/view-handler
  ; kezelőt, az editor-id azonosítóval!
  (if-let [data-received? @(r/subscribe [:item-viewer/data-received? viewer-id])]
          [:<> [:div {:style {:display "flex" :justify-content "space-between" :flex-wrap "wrap" :grid-row-gap "48px"}}
                     [:div [label       viewer-id header-props]
                           [breadcrumbs viewer-id header-props]]
                     [:div [controls    viewer-id header-props]]]
               [elements/horizontal-separator {:height :xxl}]
               (if menu-items [menu-bar viewer-id header-props])]
          [:<> [components/ghost-view {:layout :box-surface-header :breadcrumb-count (count crumbs)}]
               [:div {:style {:width "100%" :height "96px"}}]]))

;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- item-viewer
  ; @param (keyword) viewer-id
  ; @param (map) body-props
  [viewer-id body-props]
  (let [body-props (assoc body-props :error-element [components/error-content {:error  :the-item-you-opened-may-be-broken}]
                                     :ghost-element [components/ghost-view    {:layout :box-surface-body}])]
       [item-viewer/body viewer-id body-props]))

(defn item-viewer-body
  ; A komponens további paraméterezését az engines.item-viewer/body komponens
  ; dokumentácójában találod!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) body-props
  ;  {:auto-title? (boolean)(opt)
  ;    Default: true
  ;   :item-element (component or symbol)
  ;   :label-key (keyword)(opt)
  ;    Default: :name}
  ;
  ; @usage
  ;  [item-viewer-body :my-viewer {...}]
  ;
  ; @usage
  ;  (defn my-item-element [] ...)
  ;  [item-viewer-body :my-viewer {:item-element #'my-item-element}]
  [viewer-id body-props]
  (let [body-props (item-viewer.prototypes/body-props-prototype viewer-id body-props)]
       [item-viewer viewer-id body-props]))
