
(ns app.price-quotes.frontend.lister.effects
  (:require [app.price-quotes.frontend.lister.views :as lister.views]
            [re-frame.api                           :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :price-quotes.lister/load-lister!
  [:ui/render-surface! :price-quotes.lister/view
                       {:content #'lister.views/view}])
