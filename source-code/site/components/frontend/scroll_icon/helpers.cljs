
(ns site.components.frontend.scroll-icon.helpers
    (:require [x.environment.api :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn scroll-f
  ; @param (boolean) intersecting?
  [intersecting?]
  (x.environment/set-element-attribute! "mt-scroll-icon" "data-visible" intersecting?))
