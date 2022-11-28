
(ns app.website-pages.frontend.lister.effects
  (:require [app.website-pages.frontend.lister.views :as lister.views]
            [re-frame.api                            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :website-pages.lister/load-lister!
  [:x.ui/render-surface! :website-pages.lister/view
                         {:content #'lister.views/view}])
