
(ns app.components.frontend.popup-menu-header.views
    (:require [elements.api :as elements]
              [random.api   :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn close-icon-button
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;  {:on-close (metamorphic-event)}
  [_ {:keys [on-close]}]
  [elements/icon-button ::close-icon-button
                        {:hover-color :highlight
                         :keypress    {:key-code 27}
                         :on-click    on-close
                         :preset      :close}])

(defn header-label
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;  {:label (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)}
  [_ {:keys [label placeholder]}]
  [elements/label ::header-label
                  {:color       :muted
                   :content     label
                   :font-size   :xs
                   :indent      {:horizontal :xs :left :s}
                   :line-height :block
                   :placeholder placeholder}])

(defn- popup-menu-header
  ; @param (keyword) header-id
  ; @param (map) header-props
  [header-id header-props]
  [elements/horizontal-polarity ::popup-menu-header
                                {:start-content [header-label      header-id header-props]
                                 :end-content   [close-icon-button header-id header-props]}])

(defn component
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;  {:label (metamorphic-content)(opt)
  ;   :on-close (metamorphic-event)
  ;   :placeholder (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [popup-menu-header {...}]
  ;
  ; @usage
  ;  [popup-menu-header :my-popup-menu-header {...}]
  ([header-props]
   [component (random/generate-keyword) header-props])

  ([header-id header-props]
   (let []; header-props (popup-menu-header.prototypes/header-props-prototype header-props)
        [popup-menu-header header-id header-props])))
