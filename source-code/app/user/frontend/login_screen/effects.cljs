
(ns app.user.frontend.login-screen.effects
    (:require [app.user.frontend.login-screen.events :as login-screen.events]
              [app.user.frontend.login-screen.views  :as login-screen.views]
              [map.api                               :refer [dissoc-in]]
              [re-frame.api                          :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :user.login-screen/login!
  (fn [{:keys [db]} _]
      ; BUG#4677 (source-code/app/user/frontend/login_screen/subs.cljs)
      ; A valódi bejelentkezési esemény késleltetve történik, hogy ha a
      ; login-button gombra kattintás okozza az első on-mouse-down eseményt
      ; az oldalon, akkor az böngésző autofill funkciójának legyen ideje
      ; ténylegesen beleírni az értékeket a mezőkbe.
      {:db (assoc-in db [:user :login-screen/meta-items :effect-visible?] true)
       :dispatch-later [{:ms 350 :dispatch [:user.login-screen/authenticate!]}]}))

(r/reg-event-fx :user.login-screen/authenticate!
  (fn [{:keys [db]} _]
      ; BUG#4677 (source-code/app/user/frontend/login_screen/subs.cljs)
      (let [email-address (get-in db [:user :login-screen/login-data :email-address])
            password      (get-in db [:user :login-screen/login-data :password])]
           [:x.user/authenticate! {:on-failure    [:user.login-screen/authentication-failed]
                                   :email-address email-address
                                   :password      password}])))

(r/reg-event-fx :user.login-screen/authentication-failed
  ; @param (integer) status
  (fn [{:keys [db]} _]
      {:db (dissoc-in db [:user :login-screen/meta-items :effect-visible?])}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :user.login-screen/render!
  [:x.ui/render-surface! :user.login-screen/view
                         {:content #'login-screen.views/view}])
