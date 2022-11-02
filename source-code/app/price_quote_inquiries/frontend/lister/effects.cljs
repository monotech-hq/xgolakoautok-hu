
(ns app.price-quote-inquiries.frontend.lister.effects
  (:require [app.price-quote-inquiries.frontend.lister.views :as lister.views]
            [re-frame.api                                    :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :price-quote-inquiries.lister/load-lister!
  [:ui/render-surface! :price-quote-inquiries.lister/view
                       {:content #'lister.views/view}])
