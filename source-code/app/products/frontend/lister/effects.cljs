
(ns app.products.frontend.lister.effects
  (:require [app.products.frontend.lister.views :as lister.views]
            [re-frame.api                       :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :products.lister/load-lister!
  [:x.ui/render-surface! :products.lister/view
                         {:content #'lister.views/view}])
