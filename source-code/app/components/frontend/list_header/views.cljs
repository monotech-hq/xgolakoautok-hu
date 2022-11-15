
(ns app.components.frontend.list-header.views
    (:require [app.components.frontend.list-header.helpers    :as list-header.helpers]
              [app.components.frontend.list-header.prototypes :as list-header.prototypes]
              [elements.api                                   :as elements]
              [mid-fruits.random                              :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-header-body
  ; @param (keyword) header-id
  ; @param (map) header-props
  [header-id header-props]
  [:div (list-header.helpers/header-attributes header-id header-props)])

(defn- list-header
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;   :indent (map)(opt)
  ;   :style (map)(opt)}
  [header-id {:keys [class disabled? indent style] :as header-props}]
  [elements/blank header-id
                  {:class     class
                   :disabled? disabled?
                   :indent    indent
                   :content   [list-header-body header-id header-props]
                   :style     style}])

(defn component
  ; @param (keyword)(opt) header-id
  ; @param (map) header-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (map)(opt)
  ;   :style (map)(opt)}
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
