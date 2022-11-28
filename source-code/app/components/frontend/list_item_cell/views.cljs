
(ns app.components.frontend.list-item-cell.views
    (:require [app.components.frontend.list-item-cell.helpers    :as list-item-cell.helpers]
              [app.components.frontend.list-item-cell.prototypes :as list-item-cell.prototypes]
              [candy.api                                         :refer [return]]
              [css.api                                           :as css]
              [elements.api                                      :as elements]
              [random.api                                        :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-cell-body
  ; @param (keyword) cell-id
  ; @param (map) cell-props
  ;  {:rows (maps in vector)}
  [cell-id {:keys [rows] :as cell-props}]
  (letfn [(f [rows row-props]
             (if row-props (conj   rows [elements/label (assoc row-props :selectable? true)])
                           (return rows)))]
         (reduce f [:<>] rows)))

(defn- list-item-cell
  ; @param (keyword) cell-id
  ; @param (map) cell-props
  ;  {:width (px)(opt)}
  [cell-id {:keys [width] :as cell-props}]
  [:td {:style {:vertical-align "middle" :width (css/px width)}}
       [list-item-cell-body cell-id cell-props]])

(defn component
  ; @param (keyword)(opt) cell-id
  ; @param (map) cell-props
  ;  {:rows (maps in vector)
  ;    [{:color (keyword or string)(opt)
  ;       Default: :default
  ;      :content (metamorphic-content)
  ;      :font-size (keyword)(opt)
  ;        Default: :s
  ;      :font-weight (keyword)(opt)
  ;       :bold, extra-bold, :normal
  ;       Default: :bold
  ;      :placeholder (metamorphic-content)(opt)}]
  ;   :width (px)(opt)}
  ;
  ; @usage
  ;  [list-item-cell {...}]
  ;
  ; @usage
  ;  [list-item-cell :my-cell {...}]
  ;
  ; @usage
  ;  [list-item-cell :my-cell {:rows [{:content "Row #1"}]}]
  ([cell-props]
   [component (random/generate-keyword) cell-props])

  ([cell-id cell-props]
   (let [];cell-props (list-item-cell.prototypes/cell-props-prototype cell-props)
        [list-item-cell cell-id cell-props])))
