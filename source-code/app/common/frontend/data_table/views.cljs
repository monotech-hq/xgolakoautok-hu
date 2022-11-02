
(ns app.common.frontend.data-table.views
    (:require [app.common.frontend.data-table.prototypes :as data-table.prototypes]
              [elements.api                              :as elements]
              [mid-fruits.random                         :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- data-table-row-cell
  ; @param (map) cell-props
  ;  {}
  [cell-props]
  [:td {:style {:padding-right "24px"}}
       [elements/label cell-props]])

(defn- data-table-row
  ; @param (maps in vector) row-cells
  [row-cells]
  (letfn [(f [cell-list cell-props] (conj cell-list [data-table-row-cell cell-props]))]
         (reduce f [:tr] row-cells)))

(defn- data-table-rows
  ; @param (keyword) table-id
  ; @param (map) table-props
  ;  {}
  [table-id {:keys [disabled? rows] :as table-props}]
  [:table {:data-disabled disabled?}
          (letfn [(f [row-list row-cells] (conj row-list [data-table-row row-cells]))]
                 (reduce f [:tbody] rows))])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- data-table-column-cell
  ; @param (map) cell-props
  ;  {}
  [cell-props]
  [:td [elements/label cell-props]])

(defn- data-table-column
  ; @param (maps in vector) column-cells
  [column-cells]
  (letfn [(f [cell-list cell-props] (conj cell-list [data-table-column-cell cell-props]))]
         (reduce f [:tr] column-cells)))

(defn- data-table-columns
  ; @param (keyword) table-id
  ; @param (map) table-props
  ;  {}
  [table-id {:keys [columns disabled?] :as table-props}]
  [:table {:data-disabled disabled?}
          (letfn [(f [column-list column-cells] (conj column-list [data-table-column column-cells]))]
                 (reduce f [:tbody] columns))])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- data-table-label
  ; @param (keyword) table-id
  ; @param (map) table-props
  ;  {:disabled? (boolean)(opt)
  ;   :label (metamorphic-content)}
  [_ {:keys [disabled? label]}]
  (if label [elements/label {:content             label
                             :disabled?           disabled?
                             :horizontal-position :left
                             :selectable?         false}]))

(defn- data-table
  ; @param (keyword) table-id
  ; @param (map) table-props
  ;  {:indent (map)(opt)}
  [table-id {:keys [indent] :as table-props}]
  [elements/blank {:indent  indent
                   :content [:<> [data-table-label   table-id table-props]
                                 [data-table-rows    table-id table-props]
                                 [data-table-columns table-id table-props]]}])

(defn element
  ; @param (keyword)(opt) table-id
  ; @param (map) table-props
  ;  {:columns (label-props maps in vectors in vector)(opt)
  ;   :disabled? (boolean)(opt)}
  ;    Default: false
  ;   :font-size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :indent (map)(opt)
  ;   :label (metamorphic-content)
  ;   :placeholder (metamorphic-content)(opt)
  ;   :rows (label-props maps in vectors in vector)(opt)}
  ;
  ; @usage
  ;  [data-table {...}]
  ;
  ; @usage
  ;  [data-table :my-table {...}]
  ;
  ; @usage
  ;  [data-table {:rows [[{:content "Row #1"   :font-weight :extra-bold}
  ;                       {:content "Value #1" :color :muted}
  ;                       {:content "Value #2" :color :muted}]
  ;                      [...]]}]
  ([table-props]
   [element (random/generate-keyword) table-props])

  ([table-id table-props]
   (let [table-props (data-table.prototypes/table-props-prototype table-props)]
        [data-table table-id table-props])))
