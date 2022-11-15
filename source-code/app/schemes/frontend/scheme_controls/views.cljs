
(ns app.schemes.frontend.scheme-controls.views
    (:require [elements.api :as elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- add-field-button
  ; @param (keyword) scheme-id
  ; @param (map) controls-props
  ;  {:disabled? (boolean)(opt)}
  [scheme-id {:keys [disabled?]}]
  [:div {:style {:display :flex}}
        [elements/button ::add-field-button
                         {:color     :muted
                          :disabled? disabled?
                          :label     :add-field!
                          :font-size :xs
                          :indent    {:left :s}
                          :on-click  [:schemes.field-editor/load-editor! scheme-id]}]])

(defn- reorder-fields-button
  ; @param (keyword) scheme-id
  ; @param (map) controls-props
  ;  {:disabled? (boolean)(opt)}
  [scheme-id {:keys [disabled?]}]
  [:div {:style {:display :flex}}
        [elements/button ::reorder-fields-button
                         {:color     :muted
                          :disabled? disabled?
                          :label     :reorder-fields!
                          :font-size :xs
                          :indent    {:left :s}
                          :on-click  [:schemes.field-sorter/render-sorter! scheme-id]}]])

(defn- scheme-controls
  ; @param (keyword) scheme-id
  ; @param (map) controls-props
  [scheme-id controls-props]
  [:<> [reorder-fields-button scheme-id controls-props]
       [add-field-button      scheme-id controls-props]])

(defn view
  ; @param (keyword) scheme-id
  ; @param (map) controls-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false}
  [scheme-id controls-props]
  [scheme-controls scheme-id controls-props])
