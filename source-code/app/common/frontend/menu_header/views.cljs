
(ns app.common.frontend.menu-header.views
    (:require [elements.api :as elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn close-icon-button
  ; @param (keyword) popup-id
  ; @param (map) element-props
  [popup-id _]
  [elements/icon-button ::close-icon-button
                        {:hover-color :highlight
                         :keypress    {:key-code 27}
                         :on-click    [:ui/remove-popup! popup-id]
                         :preset      :close}])

(defn menu-header-label
  ; @param (keyword) popup-id
  ; @param (map) element-props
  ;  {:label (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)}
  [_ {:keys [label placeholder]}]
  [elements/label ::menu-header-label
                  {:color       :muted
                   :content     label
                   :font-size   :xs
                   :indent      {:horizontal :xs :left :s}
                   :placeholder placeholder}])

(defn- menu-header
  ; @param (keyword) popup-id
  ; @param (map) element-props
  [popup-id element-props]
  [elements/horizontal-polarity ::menu-header
                                {:start-content [menu-header-label popup-id element-props]
                                 :end-content   [close-icon-button popup-id element-props]}])

(defn element
  ; @param (keyword) popup-id
  ; @param (map) element-props
  ;  {:label (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)}
  [popup-id element-props]
  (let []; element-props (menu-header.prototypes/element-props-prototype popup-id element-props)
       [menu-header popup-id element-props]))
