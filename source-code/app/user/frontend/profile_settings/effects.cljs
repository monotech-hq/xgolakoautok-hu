
(ns app.user.frontend.profile-settings.effects
    (:require [app.user.frontend.profile-settings.views :as profile-settings.views]
              [re-frame.api                             :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :user.profile-settings/load-page!
  [:views.blank-page/load-page! :user.profile-settings/view
                                {:title :user-profile :helper :there-is-no-available-settings}])

(r/reg-event-fx :user.profile-settings/render-page!
  [:ui/render-surface! :user.profile-settings/view
                       {:content #'profile-settings.views/view}])
