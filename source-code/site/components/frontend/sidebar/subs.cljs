
(ns site.components.frontend.sidebar.subs
    (:require [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sidebar-visible?
  ; @return (boolean)
  [db _]
  (get-in db [:site.components :sidebar/meta-items :visible?]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:site.components/sidebar-visible?]
(r/reg-sub :site.components/sidebar-visible? sidebar-visible?)
