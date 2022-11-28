
(ns app.common.frontend.item-selector.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn selector-props-prototype
  ; @param (map) selector-props
  ;
  ; @return (map)
  ;  {:export-item-f (function)
  ;   :import-count-f (function)
  ;   :import-id-f (function)}
  [selector-props]
  (merge {:export-item-f  (fn [item-id item item-count] item-id)
          :import-id-f    (fn [{:keys [id]}] id)
          :import-count-f (fn [{:keys [count]}] count)}
         (param selector-props)))

(defn body-props-prototype
  ; @param (keyword) selector-id
  ; @param (map) body-props
  ;
  ; @return (map)
  ;  {:default-order-by (namespaced keyword)}
  [_ body-props]
  (merge {:default-order-by :modified-at/descending}
         (param body-props)))

(defn control-bar-props-prototype
  ; @param (keyword) selector-id
  ; @param (map) bar-props
  ;
  ; @return (map)
  ;  {:order-by-options (namespaced keywords in vector)
  ;   :search-keys (keywords in vector)}
  [_ body-props]
  (merge {:order-by-options [:modified-at/ascending :modified-at/descending :name/ascending :name/descending]
          :search-keys      [:name]}
         (param body-props)))
