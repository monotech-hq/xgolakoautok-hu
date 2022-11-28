
(ns app.components.frontend.list-item-gap.views
    (:require [css.api    :as css]
              [random.api :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-gap
  ; @param (keyword) gap-id
  ; @param (map) gap-props
  ;  {:width (px)}
  [_ {:keys [width]}]
  [:td {:style {:width (css/px width)}}])

(defn component
  ; @param (keyword)(opt) gap-id
  ; @param (map) gap-props
  ;  {:width (px)(opt)
  ;    Default: 12}
  ;
  ; @usage
  ;  [list-item-gap {...}]
  ;
  ; @usage
  ;  [list-item-gap :my-gap {...}]
  ([gap-props]
   [component (random/generate-keyword) gap-props])

  ([gap-id gap-props]
   [list-item-gap gap-id gap-props]))
