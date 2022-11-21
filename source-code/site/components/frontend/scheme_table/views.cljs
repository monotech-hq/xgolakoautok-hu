
(ns site.components.frontend.scheme-table.views
  (:require [elements.api                                  :as elements]
            [random.api                                    :as random]
            [re-frame.api                                  :as r]
            [site.components.frontend.scheme-table.helpers :as scheme-table.helpers]
            [site.components.frontend.scheme-table.queries :as scheme-table.queries]
            [vector.api                                    :as vector]
            [x.components.api                              :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- loading-label
  ; @param (keyword) component-id
  ; @param (map) component-props
  [_ _]
  [elements/label ::loading-label
   {:color       :muted
    :content     :downloading...
    :font-size   :xs
    :line-height :block
    :selectable? true}])

(defn- no-data-label
  ; @param (keyword) component-id
  ; @param (map) component-props
  ;  {:placeholder (metamorphic-content)(opt)}
  [_ {:keys [placeholder]}]
  (if placeholder [elements/label ::no-scheme-field-label
                   {:color       :muted
                    :content     placeholder
                    :font-size   :xs
                    :line-height :block
                    :selectable? true}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- scheme-field-label
  ; @param (keyword) component-id
  ; @param (map) component-props
  ; @param (metamorphic-content) field-label
  [_ _ field-label]
  [:td {:class :mt-scheme-table--field-label}
   (x.components/content field-label)])

(defn- scheme-data-value
  ; @param (keyword) component-id
  ; @param (map) component-props
  ; @param (metamorphic-content) data-value
  [_ _ data-value]
  [:td {:class :mt-scheme-table--data-value}
   (let [data-value (x.components/content data-value)]
     (if (empty? data-value)
       (str    "-")
       (str    data-value)))])

(defn- scheme-data-element
  ; @param (keyword) component-id
  ; @param (map) component-props
  ;  {:scheme-id (keyword)
  ;   :value-path (vector)}
  ; @param (keyword) field-id
  [component-id {:keys [scheme-id value-path] :as component-props} field-id]
  ; BUG#3007 (source-code/app/schemes/frontend/scheme_form/views.cljs)
  (let [field-props @(r/subscribe [:components.scheme-table/get-scheme-field scheme-id field-id])
        field-value @(r/subscribe [:x.db/get-item (vector/conj-item value-path field-id)])
        field-value (letfn [(f [value] (if-let [unit (:field/unit field-props)]
                                         {:content value :suffix (str " " unit)}
                                         {:content value}))]
                      (vector/->items field-value f))
        field-label (:field/name field-props)]
    (letfn [(f [data-values data-value]
              (conj data-values [:<> [scheme-field-label component-id component-props field-label]
                                 [scheme-data-value  component-id component-props data-value]]))]
      (if (vector/nonempty? field-value) 
        [:tr {:class :mt-scheme-data-element}
         (reduce f [:<>] field-value)]))))

(defn- scheme-fields
  ; @param (keyword) component-id
  ; @param (map) component-props
  ;  {:scheme-id (keyword)}
  [component-id {:keys [scheme-id] :as component-props}]
  (let [scheme-field-ids @(r/subscribe [:components.scheme-table/get-scheme-field-ids scheme-id])]
     (if (vector/nonempty? scheme-field-ids)
       (letfn [(f [data-list field-id] (conj data-list [scheme-data-element component-id component-props field-id]))]
         (reduce f [:<>] scheme-field-ids))
       [no-data-label component-id component-props])))

(defn- scheme-table
  ; @param (keyword) component-id
  ; @param (map) component-props
  ;  {:style (map)(opt)}
  [component-id {:keys [color style] :as component-props}]
  [:table {:class :mt-scheme-table :style style}
     [:tbody {:class :mt-scheme-table--body}
      [scheme-fields component-id component-props]]])

(defn- preloader
  ; @param (keyword) component-id
  ; @param (map) component-props
  ;  {:scheme-id (keyword)
  ;   :style (map)(opt)}
  [component-id {:keys [scheme-id] :as component-props}]
  [x.components/querier ::preloader
   {:content     [scheme-table  component-id component-props]
    :placeholder [loading-label component-id component-props]
    :query       (scheme-table.queries/request-scheme-form-query scheme-id)}])

(defn component
  ; @param (keyword)(opt) component-id
  ; @param (map) component-props
  ;  {:placeholder (metamorphic-content)(opt)
  ;   :scheme-id (keyword)
  ;   :style (map)(opt)
  ;   :value-path (vector)}
  ;
  ; @usage
  ;  [scheme-table {...}]
  ;
  ; @usage
  ;  [scheme-table :my-scheme-table {...}]
  ([component-props]
   [component (random/generate-keyword) component-props])

  ([component-id component-props]
   [preloader component-id component-props]))
