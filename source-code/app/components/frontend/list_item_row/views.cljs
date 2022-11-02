
(ns app.components.frontend.list-item-row.views
    (:require [app.components.frontend.list-item-row.helpers    :as list-item-row.helpers]
              [app.components.frontend.list-item-row.prototypes :as list-item-row.prototypes]
              [mid-fruits.random                                :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-row
  ; @param (keyword) row-id
  ; @param (map) row-props
  [row-id row-props]
  [:div (list-item-row.helpers/row-attributes row-id row-props)])

(defn component
  ; @param (keyword)(opt) row-id
  ; @param (map) row-props
  ;  {}
  ;
  ; @usage
  ;  [list-item-row {...}]
  ;
  ; @usage
  ;  [list-item-row :my-list-item-row {...}]
  ([row-props]
   [component (random/generate-keyword) row-props])

  ([row-id row-props]
   (let [row-props (list-item-row.prototypes/row-props-prototype row-props)]
        [list-item-row row-id row-props])))
