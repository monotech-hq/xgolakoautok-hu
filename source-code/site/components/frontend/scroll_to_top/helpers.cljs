
(ns site.components.frontend.scroll-to-top.helpers
    (:require [x.environment.api :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn scroll-f
  ; @param (boolean) intersecting?
  [intersecting?]
  (x.environment/set-element-attribute! "mt-scroll-to-top" "data-visible" intersecting?))
