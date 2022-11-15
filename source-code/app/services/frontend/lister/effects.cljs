
(ns app.services.frontend.lister.effects
  (:require [app.services.frontend.lister.views :as lister.views]
            [re-frame.api                       :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :services.lister/load-lister!
  [:x.ui/render-surface! :services.lister/view
                         {:content #'lister.views/view}])
