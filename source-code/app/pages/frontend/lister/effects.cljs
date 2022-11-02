
(ns app.pages.frontend.lister.effects
  (:require [app.pages.frontend.lister.views :as lister.views]
            [re-frame.api                    :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pages.lister/load-lister!
  [:ui/render-surface! :pages.lister/view
                       {:page #'lister.views/view}])
