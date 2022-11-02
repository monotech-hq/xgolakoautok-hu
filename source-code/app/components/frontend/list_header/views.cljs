
(ns app.components.frontend.list-header.views
    (:require [app.components.frontend.list-header.helpers    :as list-header.helpers]
              [app.components.frontend.list-header.prototypes :as list-header.prototypes]
              [mid-fruits.random                              :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-header
  ; @param (keyword) header-id
  ; @param (map) header-props
  [header-id header-props]
  [:div (list-header.helpers/header-attributes header-id header-props)])

(defn component
  ; @param (keyword)(opt) header-id
  ; @param (map) header-props
  ;  {}
  ;
  ; @usage
  ;  [list-header {...}]
  ;
  ; @usage
  ;  [list-header :my-list-header {...}]
  ([header-props]
   [component (random/generate-keyword) header-props])

  ([header-id header-props]
   (let [header-props (list-header.prototypes/header-props-prototype header-props)]
        [list-header header-id header-props])))
