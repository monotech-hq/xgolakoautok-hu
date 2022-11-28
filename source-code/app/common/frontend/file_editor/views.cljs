
(ns app.common.frontend.file-editor.views
    (:require [app.common.frontend.item-editor.views :as item-editor.views]
              [app.components.frontend.api           :as components]
              [elements.api                          :as elements]
              [engines.file-editor.api               :as file-editor]
              [re-frame.api                          :as r]))

;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-item-props
  ; @param (keyword) editor-id
  ; @param (map) header-props
  ; @param (map) item-props
  ;  {:change-keys (keywords in vector)(opt)
  ;   :label (metamorphic-content)}
  ;
  ; @return (map)
  ;  {:active? (boolean)
  ;   :badge-color (keyword)
  ;   :label (metamorphic-content)
  ;   :on-click (metamorphic-event)}
  [editor-id _ {:keys [change-keys label]}]
  (let [current-view @(r/subscribe [:x.gestures/get-current-view-id editor-id])
        changed? (if change-keys @(r/subscribe [:file-editor/form-changed? editor-id change-keys]))]
       {:active?     (= label current-view)
        :badge-color (if changed? :primary)
        :label       label
        :on-click    [:x.gestures/change-view! editor-id label]}))

(defn- menu-bar
  ; @param (keyword) editor-id
  ; @param (map) header-props
  ;  {:menu-items (maps in vector)}
  [editor-id {:keys [menu-items] :as header-props}]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? editor-id])]
       (letfn [(f [menu-items menu-item] (conj menu-items (menu-item-props editor-id header-props menu-item)))]
              [elements/menu-bar ::file-editor-menu-bar
                                 {:disabled?  editor-disabled?
                                  :menu-items (reduce f [] menu-items)}])))

(defn- revert-content-button
  ; @param (keyword) editor-id
  ; @param (map) header-props
  [editor-id _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? editor-id])
        content-changed? @(r/subscribe [:file-editor/content-changed? editor-id])]
       [components/surface-button ::revert-content-button
                                  {:disabled? (or editor-disabled? (not content-changed?))
                                   :on-click  [:file-editor/revert-content! editor-id]
                                   :preset    :revert}]))

(defn- save-content-button
  ; @param (keyword) editor-id
  ; @param (map) header-props
  [editor-id _]
  (let [editor-disabled? @(r/subscribe [:file-editor/editor-disabled? editor-id])]
       [components/surface-button ::save-content-button
                                  {:disabled? editor-disabled?
                                   :on-click  [:file-editor/save-content! editor-id]
                                   :preset    :save}]))

(defn- controls
  ; @param (keyword) editor-id
  ; @param (map) header-props
  [editor-id header-props]
  [:div {:style {:display "flex" :grid-column-gap "12px"}}
        [:<> [revert-content-button editor-id header-props]
             [save-content-button   editor-id header-props]]])

(defn file-editor-header
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
  ;  [file-editor-header :my-editor {...}]
  [editor-id {:keys [crumbs menu-items] :as header-props}]
  ; A menu-items használatához ne felejts el inicializálni egy gestures/view-handler
  ; kezelőt, az editor-id azonosítóval!
  (if-let [data-received? @(r/subscribe [:file-editor/data-received? editor-id])]
          [:<> [:div {:style {:display "flex" :justify-content "space-between" :flex-wrap "wrap" :grid-row-gap "48px"}}
                     [:div [item-editor.views/label       editor-id header-props]
                           [item-editor.views/breadcrumbs editor-id header-props]]
                     [:div [controls                      editor-id header-props]]]
               [elements/horizontal-separator {:height :xxl}]
               (if menu-items [menu-bar editor-id header-props])]
          [:<> [components/ghost-view {:layout :box-surface-header :breadcrumb-count (count crumbs)}]
               [:div {:style {:width "100%" :height "96px"}}]]))

;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- file-editor
  ; @param (keyword) editor-id
  ; @param (map) body-props
  [editor-id body-props]
  (let [body-props (assoc body-props :error-element [components/error-content {:error  :the-item-you-opened-may-be-broken}]
                                     :ghost-element [components/ghost-view    {:layout :box-surface-body}])]
       [file-editor/body editor-id body-props]))

(defn file-editor-body
  ; A komponens további paraméterezését az engines.file-editor/body komponens
  ; dokumentácójában találod!
  ;
  ; @param (keyword) editor-id
  ; @param (map) body-props
  ;  {:form-element (component or symbol)}
  ;
  ; @usage
  ;  [file-editor-body :my-editor {...}]
  ;
  ; @usage
  ;  (defn my-form-element [] ...)
  ;  [file-editor-body :my-editor {:form-element #'my-form-element}]
  [editor-id body-props]
  (let [];body-props (item-editor.prototypes/body-props-prototype editor-id body-props)
       [file-editor editor-id body-props]))
