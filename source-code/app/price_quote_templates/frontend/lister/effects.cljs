
(ns app.price-quote-templates.frontend.lister.effects
  (:require [app.price-quote-templates.frontend.lister.views :as lister.views]
            [re-frame.api                                    :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :price-quote-templates.lister/load-lister!
  [:x.ui/render-surface! :price-quote-templates.lister/view
                         {:content #'lister.views/view}])
