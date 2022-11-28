
(ns app.components.frontend.data-element.prototypes
    (:require [candy.api        :refer [param]]
              [vector.api       :as vector]
              [x.components.api :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-props-prototype
  ; @param (map) element-props
  ;  {:value (metamorphic-content or metamorphic-contents in vector)}
  ;
  ; @return (map)
  ;  {:font-size (keyword)
  ;   :value (metamorphic-contents in vector)}
  [{:keys [marked? value] :as element-props}]
  ; XXX#0516 (source-code/app/common/frontend/data_element/views.cljs)
  ;
  ; XXX#0510
  ; Mivel a data-element-label feliraton megjelenő {:marked? true} jelölő a value
  ; vágólapra helyezhetőségét jelzi, ezért csak akkor számít {:markable? true}
  ; állapotúnak az adat, ha vágólapra helyezhető a tartalma!
  (merge {:font-size :s}
         (param element-props)
         (cond (vector/nonempty? value) {:marked? false :value value}
               (vector?          value) {:marked? false :value [nil]}
               :return {:value [value] :marked? (and marked? (-> value x.components/content empty? not))})))
