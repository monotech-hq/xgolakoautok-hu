
(ns app.components.frontend.surface-breadcrumbs.views
    (:require [elements.api          :as elements]
              [layouts.surface-a.api :as surface-a]
              [mid-fruits.random     :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- surface-breadcrumbs
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ;  {:crumbs (maps in vector)
  ;   :disabled? (boolean)(opt)}
  [breadcrumbs-id {:keys [crumbs disabled?]}]
  [elements/breadcrumbs breadcrumbs-id
                        {:crumbs    crumbs
                         :disabled? disabled?
                         :indent    {:left :xs}}])

(defn component
  ; @param (keyword)(opt) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ;  {:crumbs (maps in vector)
  ;   :disabled? (boolean)(opt)}
  ;
  ; @usage
  ;  [surface-breadcrumbs {...}]
  ;
  ; @usage
  ;  [surface-breadcrumbs :my-surface-breadcrumbs {...}]
  ([breadcrumbs-props]
   [component (random/generate-keyword) breadcrumbs-props])

  ([breadcrumbs-id breadcrumbs-props]
   [surface-breadcrumbs breadcrumbs-id breadcrumbs-props]))
