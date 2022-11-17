
(ns app.components.frontend.item-controls.views
    (:require [app.components.frontend.surface-button.views :as surface-button.views]
              [elements.api                                 :as elements]
              [random.api                                   :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-item-button
  ; @param (keyword) controls-id
  ; @param (map) controls-props
  ;  {:disabled? (boolean)(opt)
  ;   :on-delete (metamorphic-event)(opt)}
  [controls-id {:keys [disabled? on-delete]}]
  (if on-delete [surface-button.views/component {:color       :warning
                                                 :disabled?   disabled?
                                                 :hover-color :highlight
                                                 :icon        :delete_outline
                                                 :label       :delete!
                                                 :on-click    on-delete}]))

(defn duplicate-item-button
  ; @param (keyword) controls-id
  ; @param (map) controls-props
  ;  {:disabled? (boolean)(opt)
  ;   :on-duplicate (metamorphic-event)(opt)}
  [controls-id {:keys [disabled? on-duplicate]}]
  (if on-duplicate [surface-button.views/component {:disabled?   disabled?
                                                    :hover-color :highlight
                                                    :icon        :file_copy
                                                    :icon-family :material-icons-outlined
                                                    :label       :duplicate!
                                                    :on-click    on-duplicate}]))

(defn save-item-button
  ; @param (keyword) controls-id
  ; @param (map) controls-props
  ;  {:disabled? (boolean)(opt)
  ;   :on-save (metamorphic-event)(opt)}
  [controls-id {:keys [disabled? on-save]}]
  (if on-save [surface-button.views/component {:background-color "#5a4aff"
                                               :color            "white"
                                               :disabled?        disabled?
                                               :hover-color      :highlight
                                               :icon             :save
                                               :label            :save!
                                               :on-click         on-save}]))

(defn- item-controls
  ; @param (keyword) controls-id
  ; @param (map) controls-props
  [controls-id controls-props]
  [:div {:style {:display "flex" :grid-column-gap "12px"}}
        [:<> [delete-item-button    controls-id controls-props]
             [duplicate-item-button controls-id controls-props]
             [save-item-button      controls-id controls-props]]])

(defn component
  ; @param (keyword)(opt) controls-id
  ; @param (map) controls-props
  ;  {:disabled? (boolean)(opt)
  ;   :on-delete (metamorphic-event)(opt)
  ;   :on-duplicate (metamorphic-event)(opt)
  ;   :on-save (metamorphic-event)(opt)}
  ;
  ; @usage
  ;  [item-controls {...}]
  ;
  ; @usage
  ;  [item-controls :my-item-controls {...}]
  ([controls-props]
   [component (random/generate-keyword) controls-props])

  ([controls-id controls-props]
   [item-controls controls-id controls-props]))
