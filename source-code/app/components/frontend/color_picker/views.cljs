
(ns app.components.frontend.color-picker.views
    (:require [elements.api :as elements]
              [random.api   :as random]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn color-picker-label
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:disabled? (boolean)(opt)
  ;   :label (metamorphic-content)(opt)}
  [_ {:keys [disabled? label]}]
  (if label [elements/label {:content     :color
                             :disabled?   disabled?
                             :line-height :block}]))

(defn color-picker-button
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:disabled? (boolean)(opt)
  ;   :value-path (vector)}
  [picker-id {:keys [disabled? value-path]}]
  [elements/button {:color            :muted
                    :disabled?        disabled?
                    :font-size        :xs
                    :horizontal-align :left
                    :label            :choose-color!
                    :on-click         [:elements.color-selector/render-selector! picker-id {:value-path value-path}]}])

(defn color-picker-value
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:disabled? (boolean)(opt)
  ;   :value-path (vector)}
  [_ {:keys [disabled? value-path]}]
  (let [picked-colors @(r/subscribe [:x.db/get-item value-path])]
       [elements/color-stamp {:colors    picked-colors
                              :disabled? disabled?
                              :size      :xxl}]))

(defn color-picker
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:disabled? (boolean)(opt)
  ;   :indent (map)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :value-path (vector)}
  [picker-id {:keys [indent] :as picker-props}]
  [elements/blank {:indent indent
                   :content [:<> [color-picker-label picker-id picker-props]
                                 [:div {:style {:display :flex}}
                                       [color-picker-button picker-id picker-props]]
                                 [color-picker-value picker-id picker-props]]}])

(defn component
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:disabled? (boolean)(opt)
  ;   :indent (map)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :value-path (vector)}
  ;
  ; @usage
  ;  [color-picker {...}]
  ;
  ; @usage
  ;  [color-picker :my-color-picker {...}]
  ([picker-props]
   [component (random/generate-keyword) picker-props])

  ([picker-id picker-props]
   (let [];picker-props (color-picker.prototypes/picker-props-prototype picker-props)
        [color-picker picker-id picker-props])))
