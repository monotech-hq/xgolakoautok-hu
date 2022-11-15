
(ns app.packages.frontend.lister.effects
    (:require [app.packages.frontend.lister.views :as lister.views]
              [re-frame.api                       :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :packages.lister/load-lister!
  [:x.ui/render-surface! :packages.lister/view
                         {:content #'lister.views/view}])
