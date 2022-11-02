
(ns app.views.frontend.terms-of-service.effects
    (:require [app.views.frontend.terms-of-service.views :as terms-of-service.views]
              [re-frame.api                              :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :views.terms-of-service/load-page!
  [:views.blank-page/load-page! :views.terms-of-service/view
                                {:title :terms-of-service :helper :the-content-is-not-available}])

(r/reg-event-fx :views.terms-of-service/render-page!
  [:ui/render-surface! :views.privacy-policy/view
                       {:content #'terms-of-service.views/view}])
