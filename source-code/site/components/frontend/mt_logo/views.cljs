
(ns site.components.frontend.mt-logo.views
    (:require [css.api    :as css]
              [random.api :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn mt-logo
  ; @param (keyword) component-id
  ; @param (map) component-props
  ;  {:theme (keyword)(opt)
  ;    :light, :dark
  ;    Default: :light}
  ;
  ; @usage
  ;  [mt-logo]
  [_ {:keys [theme]}]
  [:div {:style {:background-image (case theme :dark (css/url "/app/logo/mt-logo-dark.png")
                                                     (css/url "/app/logo/mt-logo-light.png"))
                 :background-size  "cover"
                 :height           "72px"
                 :width            "72px"}}])

(defn component
  ; @param (keyword)(opt) component-id
  ; @param (map) component-props
  ;  {:theme (keyword)(opt)
  ;    :light, :dark
  ;    Default: :light}
  ;
  ; @usage
  ;  [mt-logo {...}]
  ;
  ; @usage
  ;  [mt-logo :my-mt-logo {...}]
  ([component-props]
   [component (random/generate-keyword) component-props])

  ([component-id component-props]
   [mt-logo component-id component-props]))
