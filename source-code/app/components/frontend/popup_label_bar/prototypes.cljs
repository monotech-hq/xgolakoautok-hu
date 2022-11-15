
(ns app.components.frontend.popup-label-bar.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bar-props-prototype
  ; @param (map) bar-props
  ;  {:primary-button (map)
  ;   :secondary-button (map)}
  ;
  ; @return (map)
  ;  {:primary-button (map)
  ;   :secondary-button (map)}
  [{:keys [primary-button secondary-button] :as bar-props}]
  (merge {}
         (param bar-props)
         (if primary-button   {:primary-button   (merge {:color       :primary
                                                         :font-size   :xs
                                                        ;:font-weight :extra-bold
                                                         :hover-color :highlight
                                                         :indent      {:horizontal :xxs :vertical :xxs}
                                                         :keypress    {:key-code 13}}
                                                        (param primary-button))})
         (if secondary-button {:secondary-button (merge {:font-size   :xs
                                                        ;:font-weight :extra-bold
                                                         :hover-color :highlight
                                                         :indent      {:horizontal :xxs :vertical :xxs}
                                                         :keypress    {:key-code 27}}
                                                        (param secondary-button))})))
