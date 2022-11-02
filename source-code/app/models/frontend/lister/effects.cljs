
(ns app.models.frontend.lister.effects
    (:require [app.models.frontend.lister.views :as lister.views]
              [re-frame.api                     :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :models.lister/load-lister!
  [:ui/render-surface! :models.lister/view
                       {:content #'lister.views/view}])
