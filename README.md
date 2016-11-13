# Xplorer

Xplorer is a travel app that help Singapore tourists do itinerary planning,
locate popular attractions like Sentosa, and manage their trip expenses.

## Team members

1. Dominic Ong
2. Eric Teo
3. Kevin Tan
4. Shaun Yee
5. Tasya Aditya Rukmana
6. Stanley Nguyen The Hung

## App description

## Technical in-depth features analysis

**1. Daily Itinerary Planning**
  - *Greedy Search*: We start by finding the fastest route assuming user take
  public transport from the start until the end, from there we find the time-optimised
  route by moving to the closest next destination (greedy approach)
  - *Cost Approximate*: 2 scenarios:
    - If there's still available budget for transportation, we will try to change mode of
    transportation to taxi, from the most efficient flip (measured by minutes-saved/extra-dollar)
    to the least efficient flip until budget cannot accommodate extra cost for taxi.
    - If the cost already exceeds allowed budget, we will try to change mode of
    transportation to walking, from the most efficient flip (measured by dollar-saved/extra-minute)
    to the least efficient flip until it fits right in budget.

**2. Tourist Attraction Locator**
  - *Fuzzy Regular Expression*: Our search make use of the open-source Java library
  for Fuzzy Regular Expression FREJ to make close-enough predictions in case of user typo(s).
  (e.g. "sentoszza" as refering to "Sentosa")
  - *Auto-Complete Text Field*: We also made use of auto-complete text field to give hints to
  our users to make the search more robust.

**3. Expenses Manager**
 - *JSON*: We made use of JSON (JavaScript Object Notation) to locally save user entries (either directly
 from the expenses manager or the itinerary planner) to a centralised local Shared Preference.

## Future improvements

To further improve the app, we will include more attractions, in Singapore and other countries. Another
improvement is to have the app available in different languages to cater to a wider range of users/tourists.
We can also integrate real-time on-screen image translation service from Google to assist tourists. Our aim
is for it to be an all-in-one app for tourist around the world!

## Acknowledgement

*SUTD 50.001 Instructors*:
- Prof. Ngai-Man Cheung
- Prof. Jit Biswas
- Prof. Andrew Yoong
- Prof. Ng Geok See
