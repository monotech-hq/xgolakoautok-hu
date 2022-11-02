
(ns app.categories.frontend.lister.effects
    (:require [app.categories.frontend.lister.views :as lister.views]
              [engines.item-lister.api              :as item-lister]
              [re-frame.api                         :as re-frame]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(re-frame/reg-event-fx :categories.lister/load-lister!
  [:ui/render-surface! :categories.lister/view
                       {:content #'lister.views/view}])
