
(ns app.common.frontend.item-lister.helpers
    (:require [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-order-changed-f
  ; @param (keyword) lister-id
  ; @param (map) body-props
  ;  {:on-order-changed (metamorphic-event)(opt)}
  ;
  ; @return (function)
  [_ {:keys [on-order-changed]}]
  (if on-order-changed (fn [_ _ reordered-items]
                           (let [on-order-changed (r/metamorphic-event<-params on-order-changed reordered-items)]
                                (r/dispatch-sync on-order-changed)))))
