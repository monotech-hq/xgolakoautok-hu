
(ns app.price-quotes.frontend.preview.effects
  (:require [app.price-quotes.frontend.preview.views :as preview.views]
            [re-frame.api                            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :price-quotes.preview/load-preview!
  [:ui/render-popup! :price-quotes.preview/view
                     {:content #'preview.views/view}])
