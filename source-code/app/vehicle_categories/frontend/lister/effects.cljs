
(ns app.vehicle-categories.frontend.lister.effects
    (:require [app.vehicle-categories.frontend.lister.views :as lister.views]
              [engines.item-lister.api                      :as item-lister]
              [re-frame.api                                 :as re-frame]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(re-frame/reg-event-fx :vehicle-categories.lister/load-lister!
  [:x.ui/render-surface! :vehicle-categories.lister/view
                         {:content #'lister.views/view}])
