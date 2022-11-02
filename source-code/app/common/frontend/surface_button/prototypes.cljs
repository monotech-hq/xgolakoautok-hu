
(ns app.common.frontend.surface-button.prototypes
    (:require [mid-fruits.candy :refer [param]]
              [re-frame.api     :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @param (map) button-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [button-props]
  (let [viewport-large? @(r/subscribe [:environment/viewport-large?])]
       (merge {:border-radius :m
               :font-size     :xs
               :font-weight   :extra-bold}
              (param button-props)
              {:style (if viewport-large? {:line-height "48px" :padding "0 24px 0 18px"}
                                          {:line-height "36px" :padding "0 12px 0  6px"})})))
