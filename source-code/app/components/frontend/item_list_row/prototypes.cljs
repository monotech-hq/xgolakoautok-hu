
(ns app.components.frontend.item-list-row.prototypes
    (:require [candy.api  :refer [param]]
              [vector.api :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-props-prototype
  ; @param (map) row-props
  ;  {:cells (vector)}
  ;
  ; @return (map)
  ;  {:cells (vector)}
  [{:keys [cells] :as row-props}]
  ; XXX#0561
  ; A cells vektor nil értékeket is tartalmazhat a feltételesen hozzáadott celláknál,
  ; a feltétel nem teljesülésekor
  (merge {}
         (param row-props)
         {:cells (vector/remove-items-by cells nil?)}))
