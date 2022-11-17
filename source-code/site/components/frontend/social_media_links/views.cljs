
(ns site.components.frontend.social-media-links.views
    (:require [random.api :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- social-media-links
  ; @param (map) component-props
  [_]
  [:div {:class :mt-social-media-links}])

(defn component
  ; @param (map) component-props
  ;  {:links (map)
  ;   {:behance (strings in vector)
  ;    :facebook (strings in vector)
  ;    :github (strings in vector)
  ;    :instagram (strings in vector)
  ;    :linkedin (strings in vector)
  ;    :youtube (strings in vector)
  ;    :twitter (strings in vector)}
  ;   :show-label? (boolean)(opt)
  ;    Default: false}
  ;
  ; @usage
  ;  [social-media-links {...}]
  ;
  ; @usage
  ;  [social-media-links :my-social-media-links {...}]
  ([component-props]
   [component (random/generate-keyword) component-props])

  ([component-id component-props]
   [social-media-links component-props]))
