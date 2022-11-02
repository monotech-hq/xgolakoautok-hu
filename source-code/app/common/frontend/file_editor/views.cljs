
(ns app.common.frontend.file-editor.views
    (:require [app.common.frontend.item-editor.views    :as item-editor.views]
              [app.common.frontend.surface.views        :as surface.views]
              [app.common.frontend.surface-button.views :as surface-button.views]
              [elements.api                             :as elements]
              [re-frame.api                             :as r]))

;; -- Menu-bar components -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-editor-menu-item-props
  ; @param (keyword) editor-id
  ; @param (map) bar-props
  ; @param (map) item-props
  ;  {:change-keys (keywords in vector)(opt)
  ;   :label (metamorphic-content)}
  ;
  ; @usage
  ;  [file-editor-menu-item-props :my-editor {...} {...}]
  ;
  ; @return (map)
  ;  {:active? (boolean)
  ;   :badge-color (keyword)
  ;   :label (metamorphic-content)
  ;   :on-click (metamorphic-event)}
  [editor-id _ {:keys [change-keys label]}]
  (let [current-view @(r/subscribe [:gestures/get-current-view-id editor-id])
        changed? (if change-keys @(r/subscribe [:file-editor/form-changed? editor-id change-keys]))]
       {:active?     (= label current-view)
        :badge-color (if changed? :primary)
        :label       label
        :on-click    [:gestures/change-view! editor-id label]}))

(defn file-editor-menu-bar
  ; A komponens használatához ne felejts el inicializálni egy gestures/view-handler
  ; kezelőt, az editor-id azonosítóval!
  ;
  ; @param (keyword) editor-id
  ; @param (map) bar-props
  ;  {:disabled? (boolean)(opt)
  ;   :menu-items (maps in vector)
  ;    [{:change-keys (keywords in vector)(opt)
  ;      :label (metamorphic-content)}]}
  ;
  ; @usage
  ;  [file-editor-menu-bar :my-editor {...}]
  [editor-id {:keys [disabled? menu-items] :as bar-props}]
  (letfn [(f [menu-items menu-item] (conj menu-items (file-editor-menu-item-props editor-id bar-props menu-item)))]
         [:<> [elements/menu-bar ::file-editor-menu-bar {:disabled?  disabled?
                                                         :menu-items (reduce f [] menu-items)}]]))
              ;[elements/horizontal-line {:color :highlight :indent {:vertical :s}}]]))

;; -- Ghost-view components ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-editor-ghost-element
  ; @param (keyword) editor-id
  ; @param (map) element-props
  ;
  ; @usage
  ;  [file-editor-ghost-element :my-editor {...}]
  [editor-id _]
  [surface.views/surface-box-layout-ghost-view editor-id {:breadcrumb-count 2}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn revert-content-button
  ; @param (keyword) editor-id
  ; @param (map) bar-props
  ;
  ; @usage
  ;  [revert-content-button :my-editor {...}]
  [editor-id {:keys [disabled?]}]
  (let [content-changed? @(r/subscribe [:file-editor/content-changed? editor-id])]
       [surface-button.views/element ::revert-content-button
                                     {:disabled?   (or disabled? (not content-changed?))
                                      :hover-color :highlight
                                      :icon        :settings_backup_restore
                                      :label       :revert!
                                      :on-click    [:file-editor/revert-content! editor-id]}]))

(defn save-content-button
  ; @param (keyword) editor-id
  ; @param (map) bar-props
  ;
  ; @usage
  ;  [save-content-button :my-editor {...}]
  [editor-id {:keys [disabled?]}]
  [surface-button.views/element ::save-content-button
                                {:background-color "#5a4aff"
                                 :color            "white"
                                 :disabled?        disabled?
                                 :icon             :save
                                 :label            :save!
                                 :on-click         [:file-editor/save-content! editor-id]}])

(defn file-editor-controls
  ; @param (keyword) editor-id
  ; @param (map) bar-props
  ;
  ; @usage
  ;  [file-editor-controls :my-editor {...}]
  [editor-id bar-props]
  [:div {:style {:display "flex" :grid-column-gap "12px"}}
        [:<> [revert-content-button editor-id bar-props]
             [save-content-button   editor-id bar-props]]])
