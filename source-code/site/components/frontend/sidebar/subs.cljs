
(ns site.components.frontend.sidebar.subs
    (:require [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sidebar-visible?
  ; @return (boolean)
  [db _]
  (get-in db [:components :sidebar/meta-items :visible?]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:components.sidebar/sidebar-visible?]
(r/reg-sub :components.sidebar/sidebar-visible? sidebar-visible?)
