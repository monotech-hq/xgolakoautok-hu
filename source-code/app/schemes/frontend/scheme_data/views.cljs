
(ns app.schemes.frontend.scheme-data.views
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [elements.api                :as elements]
              [map.api                     :as map]
              [re-frame.api                :as r]
              [vector.api                  :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- no-scheme-field-label
  ; @param (keyword) scheme-id
  ; @param (map) data-props
  ;  {:disabled? (boolean)(opt)}
  [_ {:keys [disabled?]}]
  [elements/label ::no-scheme-field-label
                  {:color            :highlight
                   :content          :no-field-created
                   :disabled?        disabled?
                   :font-size        :xs
                   :horizontal-align :center
                   :line-height      :block}])

(defn- scheme-data-element
  ; @param (keyword) scheme-id
  ; @param (map) data-props
  ;  {:value-path (vector)}
  ; @param (keyword) field-id
  [scheme-id {:keys [disabled? value-path]} field-id]
  ; BUG#3007 (source-code/app/schemes/frontend/scheme_form/views.cljs)
  (let [field-props @(r/subscribe [:schemes.form-handler/get-scheme-field scheme-id field-id])
        field-value @(r/subscribe [:x.db/get-item (vector/conj-item value-path field-id)])
        field-value (letfn [(f [value] (if-let [unit (:field/unit field-props)]
                                               {:content value :suffix (str " " unit)}
                                               {:content value}))]
                           (vector/->items field-value f))]
       [components/data-element {:disabled?   disabled?
                                 :indent      {:top :m :vertical :s}
                                 :label       (:field/name field-props)
                                 :placeholder "n/a"
                                 :value       field-value}]))

(defn- scheme-data
  ; @param (keyword) scheme-id
  ; @param (map) data-props
  [scheme-id data-props]
  (let [scheme-field-ids @(r/subscribe [:schemes.form-handler/get-scheme-field-ids scheme-id])]
       (if (vector/nonempty? scheme-field-ids)
           (letfn [(f [data-list field-id] (conj data-list [scheme-data-element scheme-id data-props field-id]))]
                  (reduce f [:<>] scheme-field-ids))
           [no-scheme-field-label scheme-id data-props])))

(defn view
  ; @param (keyword) scheme-id
  ; @param (map) data-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :value-path (vector)(opt)}
  ;
  ; @usage
  ;  [scheme-data :my-scheme {:value-path [:my-values]}]
  [scheme-id data-props]
  [scheme-data scheme-id data-props])
