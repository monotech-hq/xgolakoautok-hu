
(ns app.frame.frontend.side-effects
    (:require [app.frame.frontend.state :as state]
              [re-frame.api             :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn show-menu!
  [menu-id]
  (reset! state/VISIBLE-MENU menu-id))

(defn hide-menu!
  [_]

  (reset! state/VISIBLE-MENU nil))

(defn toggle-menu!
  [menu-id]
  (if (= menu-id @state/VISIBLE-MENU) (hide-menu! menu-id)
                                      (show-menu! menu-id)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-fx :frame/show-menu!   show-menu!)
(r/reg-fx :frame/hide-menu!   hide-menu!)
(r/reg-fx :frame/toggle-menu! toggle-menu!)
