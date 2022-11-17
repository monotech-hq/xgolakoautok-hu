
(ns site.components.frontend.follow-us.helpers
    (:require [x.environment.api :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn scroll-f
  ; @param (boolean) intersecting?
  [intersecting?]
  (x.environment/set-element-attribute! "mt-follow-us" "data-scrolled" (not intersecting?)))
