
(ns app.rental-vehicles.frontend.lister.effects
    (:require [app.rental-vehicles.frontend.lister.views :as lister.views]
              [re-frame.api                              :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :rental-vehicles.lister/load!
  [:x.ui/render-surface! :rental-vehicles.lister/view
                         {:content #'lister.views/view}])
