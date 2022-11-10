
(ns site.xgo.pages.main-page.frontend.effects
    (:require [re-frame.api                        :as r]
              [site.xgo.pages.main-page.frontend.views :as views]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(r/reg-event-fx :main-page/render-page!
  [:ui/render-surface! :main-page/view {:content #'views/view}])

(r/reg-event-fx :main-page/load-page!
  [:main-page/render-page!])
