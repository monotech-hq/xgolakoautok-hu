
(ns app.components.frontend.surface-button.prototypes
    (:require [candy.api    :refer [param]]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @param (map) button-props
  ;
  ; @return (map)
  ;  {:border-radius (keyword)
  ;   :font-size (keyword)
  ;   :font-weight (keyword)
  ;   :style (map)}
  [button-props]
  (let [viewport-large? @(r/subscribe [:x.environment/viewport-large?])]
       (merge {:border-radius :xxl
               :font-size     :xs
               :font-weight   :extra-bold}
              (param button-props)
              {:style (if viewport-large? {:height "48px" :padding "0 24px 0 18px"}
                                          {:height "36px" :padding "0 12px 0  6px"})})))
