
(ns app.user.frontend.create-account.effects
    (:require [app.user.frontend.create-account.views :as create-account.views]
              [re-frame.api                           :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :user.create-account/render!
  [:ui/render-surface! :user.create-account/view
                       {:content #'create-account.views/view}])
