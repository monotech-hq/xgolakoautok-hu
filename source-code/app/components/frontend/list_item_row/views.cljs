
(ns app.components.frontend.list-item-row.views
    (:require [app.components.frontend.list-item-row.helpers    :as list-item-row.helpers]
              [app.components.frontend.list-item-row.prototypes :as list-item-row.prototypes]
              [elements.api                                     :as elements]
              [mid-fruits.random                                :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-row-body
  ; @param (keyword) row-id
  ; @param (map) row-props
  [row-id row-props]
  [:div (list-item-row.helpers/row-attributes row-id row-props)])

(defn- list-item-row
  ; @param (keyword) row-id
  ; @param (map) row-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;   :indent (map)(opt)
  ;   :style (map)(opt)}
  [row-id {:keys [class disabled? indent style] :as row-props}]
  [elements/blank row-id
                  {:class     class
                   :disabled? disabled?
                   :indent    indent
                   :content   [list-item-row-body row-id row-props]
                   :style     style}])

(defn component
  ; @param (keyword)(opt) row-id
  ; @param (map) row-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (map)(opt)
  ;   :style (map)(opt)}
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
