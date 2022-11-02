
(ns app.components.frontend.list-item-cell.views
    (:require [app.components.frontend.list-item-cell.helpers    :as list-item-cell.helpers]
              [app.components.frontend.list-item-cell.prototypes :as list-item-cell.prototypes]
              [mid-fruits.random                                 :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-cell
  ; @param (keyword) cell-id
  ; @param (map) cell-props
  [cell-id cell-props]
  [:div (list-item-cell.helpers/cell-attributes cell-id cell-props)])

(defn component
  ; @param (keyword)(opt) cell-id
  ; @param (map) cell-props
  ;  {}
  ;
  ; @usage
  ;  [list-item-cell {...}]
  ;
  ; @usage
  ;  [list-item-cell :my-list-item-cell {...}]
  ([cell-props]
   [component (random/generate-keyword) cell-props])

  ([cell-id cell-props]
   (let [cell-props (list-item-cell.prototypes/cell-props-prototype cell-props)]
        [list-item-cell cell-id cell-props])))
