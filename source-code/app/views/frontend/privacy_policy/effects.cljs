
(ns app.views.frontend.privacy-policy.effects
    (:require [app.views.frontend.privacy-policy.views :as privacy-policy.views]
              [re-frame.api                            :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :views.privacy-policy/load-page!
  [:views.blank-page/load-page! :views.privacy-policy/view
                                {:title :privacy-policy :helper :the-content-is-not-available}])

(r/reg-event-fx :views.privacy-policy/render-page!
  [:x.ui/render-surface! :views.privacy-policy/view
                         {:content #'privacy-policy.views/view}])
