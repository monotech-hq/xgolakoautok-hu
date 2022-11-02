
(ns app.user.frontend.forgot-password.effects
    (:require [app.user.frontend.forgot-password.views :as forgot-password.views]
              [re-frame.api                            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :user.forgot-password/render!
  [:ui/render-surface! :user.forgot-password/view
                       {:content #'forgot-password.views/view}])
